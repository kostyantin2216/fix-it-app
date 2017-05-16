package com.fixit.core.utils;

import com.fixit.core.data.Review;
import com.fixit.core.data.ReviewData;
import com.fixit.core.data.Tradesman;
import com.fixit.core.data.TradesmanWrapper;
import com.fixit.core.data.WorkingDay;
import com.fixit.core.data.WorkingHours;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by konstantin on 4/27/2017.
 */

public class ObjectGenerator {

    public static ArrayList<TradesmanWrapper> createTradesmenWrappers(int count) {
        ArrayList<TradesmanWrapper> result = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            result.add(new TradesmanWrapper(createTradesman(), 123));
        }
        return result;
    }

    public static Tradesman createTradesman() {
        Tradesman tradesman = new Tradesman();
        tradesman.setCompanyName("Professional Plumbing");
        tradesman.setWorkingDays(createWorkingDays());
        tradesman.setRating(3.5f);
        return tradesman;
    }

    public static WorkingDay[] createWorkingDays() {
        WorkingDay[] workingDays = new WorkingDay[5];
        workingDays[0] = new WorkingDay(1, new WorkingHours[]{ new WorkingHours(8.50, 12.00), new WorkingHours(13.00, 21.00) });
        workingDays[1] = new WorkingDay(2, new WorkingHours[]{ new WorkingHours(8.50, 12.00), new WorkingHours(13.00, 21.00) });
        workingDays[2] = new WorkingDay(3, new WorkingHours[]{ new WorkingHours(8.50, 12.00), new WorkingHours(13.00, 21.00) });
        workingDays[3] = new WorkingDay(4, new WorkingHours[]{ new WorkingHours(8.50, 12.00), new WorkingHours(13.00, 21.00) });
        workingDays[4] = new WorkingDay(5, new WorkingHours[]{ new WorkingHours(8.50, 12.00), new WorkingHours(13.00, 21.00) });
        return workingDays;
    }

    public static Review createReview() {
        Review review = new Review();
        review.setRating(4.5f);
        review.setTitle("the best");
        review.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer non eleifend odio. Cras et sodales nunc, in gravida mauris. Morbi vel rhoncus purus. Mauris eu diam quis nisi sagittis tincidunt. Sed vel ullamcorper nulla, non pharetra nulla. In id cursus ante, eget laoreet leo. Curabitur mattis nisl at sem iaculis, vel consequat sem mollis. Phasellus interdum ante eu suscipit venenatis. Suspendisse mi turpis, aliquet ac lacus sed, suscipit venenatis est. Donec efficitur turpis id felis faucibus vestibulum. Curabitur sed viverra ante. Mauris turpis eros, venenatis in fermentum in, malesuada non enim.\n" +
                "\n" +
                "Proin vel mattis tellus. Curabitur sapien elit, fermentum id turpis ac, dapibus placerat orci. Duis maximus varius tortor non rutrum. In sodales mi vitae commodo dignissim. Donec ut sapien vel justo ultricies semper. Suspendisse lacinia purus in purus varius, sit amet tempus ligula malesuada. Praesent viverra ante tempus nunc vestibulum elementum. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nunc erat mi, elementum a malesuada eget, aliquet ac odio. In vehicula rutrum ex sit amet venenatis. Proin metus lorem, pulvinar quis erat sit amet, dapibus pharetra leo. Morbi nunc justo, sollicitudin vitae nibh in, egestas tempor nulla. Etiam ornare eleifend nulla et fermentum. Etiam facilisis massa congue arcu volutpat, ut dapibus enim bibendum.");
        review.setCreatedAt(new Date());
        review.setUserId("123");
        review.setTradesmanId("123");
        return review;
    }

    public static List<ReviewData> createReviewData(int count) {
        List<ReviewData> result = new ArrayList<>(count);
        for(int i = 0; i < count; i++) {
            result.add(new ReviewData(createReview(), "Bob Marley", "https://s-media-cache-ak0.pinimg.com/736x/4c/1e/a5/4c1ea5b6a1d8f17f3bf4fcfcedb4e423.jpg"));
        }
        return result;
    }
}
