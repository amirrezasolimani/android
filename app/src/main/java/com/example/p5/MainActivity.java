package com.example.p5;

import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextView tvCounter, tvScore, tvMistakes, tvWord, tvHint;
    private GridLayout lettersGrid;

    // Room
    private AppDb db;
    private WordDao dao;
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();

    // session یعنی یک دور بازی (۵ سوال). کلمات فقط در همین session تکراری نمی‌شن
    private long sessionId;

    // 32 حرف فارسی
    private static final char[] PERSIAN_LETTERS = new char[]{
            'ا','ب','پ','ت','ث','ج','چ','ح',
            'خ','د','ذ','ر','ز','ژ','س','ش',
            'ص','ض','ط','ظ','ع','غ','ف','ق',
            'ک','گ','ل','م','ن','و','ه','ی'
    };

    private int questionIndex = 0; // 0..4
    private static final int TOTAL_QUESTIONS = 5;

    private int score = 0;

    // وضعیت سوال فعلی
    private WordEntity currentItem;
    private String currentWord;   // نرمال شده (بدون فاصله/نیم فاصله، ی/ک درست)
    private boolean[] revealed;
    private int mistakes = 0;
    private static final int MAX_MISTAKES = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCounter = findViewById(R.id.tvCounter);
        tvScore = findViewById(R.id.tvScore);
        tvMistakes = findViewById(R.id.tvMistakes);
        tvWord = findViewById(R.id.tvWord);
        tvHint = findViewById(R.id.tvHint);
        lettersGrid = findViewById(R.id.lettersGrid);

        db = AppDb.getInstance(this);
        dao = db.wordDao();

        buildLetterButtons();

        startNewSession(); // شروع بازی
    }

    private void startNewSession() {
        sessionId = System.currentTimeMillis();
        questionIndex = 0;
        score = 0;
        loadNextQuestionFromDb();
    }

    // ساخت 32 دکمه داخل GridLayout
    private void buildLetterButtons() {
        lettersGrid.removeAllViews();

        for (char letter : PERSIAN_LETTERS) {
            Button b = new Button(this);
            b.setText(String.valueOf(letter));
            b.setAllCaps(false);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            b.setPadding(0, 0, 0, 0);
            b.setGravity(Gravity.CENTER);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            lp.setMargins(6, 6, 6, 6);
            b.setLayoutParams(lp);

            b.setOnClickListener(v -> onLetterClicked(b));

            lettersGrid.addView(b);
        }
    }

    private void onLetterClicked(Button button) {
        String s = button.getText().toString();
        if (s.isEmpty()) return;

        char chosen = s.charAt(0);
        button.setEnabled(false);

        boolean hit = revealMatches(chosen);

        if (!hit) {
            mistakes++;
            tvHint.setText("اشتباه ❌");
        } else {
            tvHint.setText("درست ✅");
        }

        updateUIWord();
        updateHeaderUI();

        // کلمه کامل شد
        if (isWordComplete()) {
            // امتیاز: 20 پایه + بونوس به ازای کم‌اشتباه‌تر بودن
            int bonus = Math.max(0, (MAX_MISTAKES - mistakes) * 2);
            score += 20 + bonus;

            tvHint.setText("کلمه کامل شد 🎉");
            disableAllLetterButtons(true);

            new Handler().postDelayed(this::goNextOrFinish, 800);
            return;
        }

        // باخت در این کلمه
        if (mistakes >= MAX_MISTAKES) {
            tvHint.setText("باختی! جواب: " + currentWord);
            disableAllLetterButtons(true);

            new Handler().postDelayed(this::goNextOrFinish, 1200);
        }
    }

    private boolean revealMatches(char chosen) {
        boolean hit = false;
        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == chosen) {
                revealed[i] = true;
                hit = true;
            }
        }
        return hit;
    }

    // ✅ کلمه بعدی از Room (غیرمسدود + استفاده‌نشده در session)
    private void loadNextQuestionFromDb() {
        tvHint.setText("در حال آماده‌سازی سوال...");

        dbExecutor.execute(() -> {
            WordEntity w = dao.getRandomWord("geography", sessionId);

            if (w != null) {
                // ثبت استفاده در همین session (برای جلوگیری از تکرار)
                dao.markUsed(new UsedWordEntity(sessionId, w.id));

                runOnUiThread(() -> setNewWord(w));
            } else {
                // اگر کلمه‌ای نمانده (مثلاً همه blocked یا در session مصرف شده)
                runOnUiThread(this::finishGame);
            }
        });
    }

    private void setNewWord(WordEntity w) {
        currentItem = w;
        currentWord = normalizePersian(w.text);

        revealed = new boolean[currentWord.length()];
        mistakes = 0;

        // اگر کاراکتر غیرحرف بود، خودش باز باشه (اینجا عملاً نیاز نیست ولی امنه)
        for (int i = 0; i < currentWord.length(); i++) {
            char c = currentWord.charAt(i);
            if (!isPersianLetter(c)) revealed[i] = true;
        }

        enableAllLetterButtons();
        updateUIWord();
        updateHeaderUI();

        tvHint.setText("حروف رو انتخاب کن");
    }

    private void updateUIWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < currentWord.length(); i++) {
            sb.append(revealed[i] ? currentWord.charAt(i) : '＿');
            if (i < currentWord.length() - 1) sb.append(' ');
        }
        tvWord.setText(sb.toString());
    }

    private void updateHeaderUI() {
        tvCounter.setText("سوال " + (questionIndex + 1) + " از " + TOTAL_QUESTIONS);
        tvScore.setText("امتیاز: " + score);
        tvMistakes.setText("اشتباه: " + mistakes + " / " + MAX_MISTAKES);
    }

    private boolean isWordComplete() {
        for (boolean r : revealed) if (!r) return false;
        return true;
    }

    private void goNextOrFinish() {
        questionIndex++;
        if (questionIndex >= TOTAL_QUESTIONS) {
            finishGame();
        } else {
            loadNextQuestionFromDb();
        }
    }

    // ✅ پایان بازی: used_words همین session پاک می‌شه => کلمات "نمی‌سوزن"
    private void finishGame() {
        dbExecutor.execute(() -> dao.clearUsedForSession(sessionId));

        new AlertDialog.Builder(this)
                .setTitle("پایان بازی")
                .setMessage("امتیاز نهایی شما: " + score + "\n(" + TOTAL_QUESTIONS + " کلمه)")
                .setCancelable(false)
                .setPositiveButton("شروع دوباره", (d, which) -> startNewSession())
                .show();
    }

    private void enableAllLetterButtons() {
        for (int i = 0; i < lettersGrid.getChildCount(); i++) {
            lettersGrid.getChildAt(i).setEnabled(true);
        }
    }

    private void disableAllLetterButtons(boolean disable) {
        for (int i = 0; i < lettersGrid.getChildCount(); i++) {
            lettersGrid.getChildAt(i).setEnabled(!disable);
        }
    }

    private boolean isPersianLetter(char c) {
        for (char p : PERSIAN_LETTERS) if (p == c) return true;
        return false;
    }

    // نرمال‌سازی برای جلوگیری از باگ‌های ي/ك و فاصله/نیم‌فاصله
    private String normalizePersian(String s) {
        if (s == null) return "";
        return s.replace('ي', 'ی')
                .replace('ك', 'ک')
                .replace("‌", "")
                .replace(" ", "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbExecutor.shutdown();
    }
}
