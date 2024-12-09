package org.example.proiect_gradle.Repository.FileRepository;

import org.example.proiect_gradle.Domain.Review;

public class ReviewFileRepository extends FileRepository<Review> {

    public ReviewFileRepository(String filename){
        super(filename);
    }

    protected String convertObjectToString(Review review){

        if(review==null){
            throw new IllegalArgumentException("Review object cannot be null");
        }

        return review.getId()+ "," +
                review.getGrade()+ "," +
                review.getMessage()+ "," +
                review.getReviewer()+ "," +
                review.getReviewee();
    }

    protected Review createObjectFromString(String line){

        if(line==null || line.trim().isEmpty()){
            throw new IllegalArgumentException("Line to parse cannot be null or empty");
        }

        try {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            double grade = Double.parseDouble(parts[1]);
            String message = parts[2];
            int reviewer = Integer.parseInt(parts[3]);
            int reviewee = Integer.parseInt(parts[4]);
            Review review = new Review(grade, message, reviewer, reviewee);
            review.setId(id);
            return review;
        }catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Error parsing user data: " + line, e);
        }
    }
}
