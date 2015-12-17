package extension;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.akili.etc.triviacrashsaga.R;
import com.akili.etc.triviacrashsaga.Singleton.AchievementSystem;

/**
 * Created by kangleif on 11/29/2015.
 */
public class ResourceTool {

    public static String getQuizTitle(AchievementSystem.CategoryType categoryType) {
        if (categoryType == AchievementSystem.CategoryType.DOCTOR)
            return "Doctors solve medical problems. They have to have a lot of knowledge about science. Doctors are also good at handling stress and working with people from different backgrounds.  Think you can be a doctor?";
        else if (categoryType == AchievementSystem.CategoryType.TEACHER)
            return "Teachers guide young people in learning new things. Teachers need a lot of knowledge about many topics. They also are leaders in their classrooms. Try out these Teacher Challenges.";
        else if (categoryType == AchievementSystem.CategoryType.WRITER)
            return "Writers create stories in different forms, such as novels or movies. Writers know a lot about language. They also have listening and collaborations skills. Why not try these Writer Challenges?";
        else if (categoryType == AchievementSystem.CategoryType.GAMER)
            return "Game Designers create interesting and fun game mechanics. They may make games for the computer, phones, or board games. Designers know a lot about human psychology. They are good at understanding what others are thinking and collaborating. Try these Game Design Challenges.";
        else if (categoryType == AchievementSystem.CategoryType.JOURNALIST)
            return "Journalists investigate events in order to write news. They may work for newspapers, Television, or write books. Journalists know how to communicate news events well and separate facts from opinion. They know how to talk to others and are leaders. Check out these Journalist Challenges.";
        else if (categoryType == AchievementSystem.CategoryType.ARTIST)
            return "Artists create works of art. They may display their work in a museum or create art for books, games, or advertisements. Artists know a lot about color, light, and how the body works. They also work are good at understanding and expressing emotions in interesting ways. Try the Artist Challenges!";

        return "Artists create works of art. They may display their work in a museum or create art for books, games, or advertisements. Artists know a lot about color, light, and how the body works. They also work are good at understanding and expressing emotions in interesting ways. Try the Artist Challenges!";
    }

    public static int getColorRef(Context c, AchievementSystem.CategoryType categoryType){
        if (categoryType == AchievementSystem.CategoryType.DOCTOR)
            return c.getResources().getColor(R.color.category_doctor_color);
        else if (categoryType == AchievementSystem.CategoryType.TEACHER)
            return c.getResources().getColor(R.color.category_teacher_color);
        else if (categoryType == AchievementSystem.CategoryType.WRITER)
            return c.getResources().getColor(R.color.category_writer_color);
        else if (categoryType == AchievementSystem.CategoryType.GAMER)
            return c.getResources().getColor(R.color.category_writer_color);
        else if (categoryType == AchievementSystem.CategoryType.JOURNALIST)
            return c.getResources().getColor(R.color.category_writer_color);
        else if (categoryType == AchievementSystem.CategoryType.ARTIST)
            return c.getResources().getColor(R.color.category_teacher_color);

        return c.getResources().getColor(R.color.category_doctor_color);
    }

    public static int getShadowColor(Context c, AchievementSystem.CategoryType categoryType){
        if (categoryType == AchievementSystem.CategoryType.DOCTOR)
            return c.getResources().getColor(R.color.custom_progress_doctor);
        else if (categoryType == AchievementSystem.CategoryType.TEACHER)
            return c.getResources().getColor(R.color.custom_progress_teacher);
        else if (categoryType == AchievementSystem.CategoryType.WRITER)
            return c.getResources().getColor(R.color.custom_progress_writer);
        else if (categoryType == AchievementSystem.CategoryType.GAMER)
            return c.getResources().getColor(R.color.custom_progress_writer);
        else if (categoryType == AchievementSystem.CategoryType.JOURNALIST)
            return c.getResources().getColor(R.color.custom_progress_writer);
        else if (categoryType == AchievementSystem.CategoryType.ARTIST)
            return c.getResources().getColor(R.color.custom_progress_teacher);

        return c.getResources().getColor(R.color.custom_progress_doctor);
    }


    public static Drawable getChallengeIcon(Context c, AchievementSystem.CategoryType categoryType) {
        if (categoryType == AchievementSystem.CategoryType.DOCTOR)
            return c.getResources().getDrawable(R.drawable.challenge_doctor);
        else if (categoryType == AchievementSystem.CategoryType.TEACHER)
            return c.getResources().getDrawable(R.drawable.challenge_teacher);
        else if (categoryType == AchievementSystem.CategoryType.WRITER)
            return c.getResources().getDrawable(R.drawable.challenge_writer);
        else if (categoryType == AchievementSystem.CategoryType.GAMER)
            return c.getResources().getDrawable(R.drawable.challenge_gamer);
        else if (categoryType == AchievementSystem.CategoryType.JOURNALIST)
            return c.getResources().getDrawable(R.drawable.challenge_journalist);
        else if (categoryType == AchievementSystem.CategoryType.ARTIST)
            return c.getResources().getDrawable(R.drawable.challenge_artist);

        return c.getResources().getDrawable(R.drawable.id_doctor);
    }

    public static Drawable getColoredChallengeIcon(Context c, AchievementSystem.CategoryType categoryType) {
        if (categoryType == AchievementSystem.CategoryType.DOCTOR)
            return c.getResources().getDrawable(R.drawable.root_challenge_doctor);
        else if (categoryType == AchievementSystem.CategoryType.TEACHER)
            return c.getResources().getDrawable(R.drawable.root_challenge_teacher);
        else if (categoryType == AchievementSystem.CategoryType.WRITER)
            return c.getResources().getDrawable(R.drawable.root_challenge_writer);
        else if (categoryType == AchievementSystem.CategoryType.GAMER)
            return c.getResources().getDrawable(R.drawable.root_challenge_gamer);
        else if (categoryType == AchievementSystem.CategoryType.JOURNALIST)
            return c.getResources().getDrawable(R.drawable.root_challenge_journalist);
        else if (categoryType == AchievementSystem.CategoryType.ARTIST)
            return c.getResources().getDrawable(R.drawable.root_challenge_artist);

        return c.getResources().getDrawable(R.drawable.id_doctor);
    }

    public static Drawable getIdentityIcon(Context c, AchievementSystem.CategoryType categoryType) {
        if (categoryType == AchievementSystem.CategoryType.DOCTOR)
            return c.getResources().getDrawable(R.drawable.id_doctor);
        else if (categoryType == AchievementSystem.CategoryType.TEACHER)
            return c.getResources().getDrawable(R.drawable.id_teacher);
        else if (categoryType == AchievementSystem.CategoryType.WRITER)
            return c.getResources().getDrawable(R.drawable.id_writer);
        else if (categoryType == AchievementSystem.CategoryType.GAMER)
            return c.getResources().getDrawable(R.drawable.id_game_designer);
        else if (categoryType == AchievementSystem.CategoryType.JOURNALIST)
            return c.getResources().getDrawable(R.drawable.id_journalist);
        else if (categoryType == AchievementSystem.CategoryType.ARTIST)
            return c.getResources().getDrawable(R.drawable.id_artist);

        return c.getResources().getDrawable(R.drawable.id_doctor);
    }

    public static Drawable getBackGroundImage(Context c, AchievementSystem.CategoryType categoryType) {
        if (categoryType == AchievementSystem.CategoryType.DOCTOR)
            return c.getResources().getDrawable(R.drawable.background_doctor);
        else if (categoryType == AchievementSystem.CategoryType.TEACHER)
            return c.getResources().getDrawable(R.drawable.background_teacher);
        else if (categoryType == AchievementSystem.CategoryType.WRITER)
            return c.getResources().getDrawable(R.drawable.background_writer);
        else if (categoryType == AchievementSystem.CategoryType.GAMER)
            return c.getResources().getDrawable(R.drawable.background_gamer);
        else if (categoryType == AchievementSystem.CategoryType.JOURNALIST)
            return c.getResources().getDrawable(R.drawable.background_journalist);
        else if (categoryType == AchievementSystem.CategoryType.ARTIST)
            return c.getResources().getDrawable(R.drawable.background_artist);

        return c.getResources().getDrawable(R.drawable.background_doctor);
    }
}
