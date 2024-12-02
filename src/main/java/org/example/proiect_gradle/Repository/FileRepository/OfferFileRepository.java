package org.example.proiect_gradle.Repository.FileRepository;

import org.example.proiect_gradle.Domain.Offer;

public class OfferFileRepository extends FileRepository<Offer> {

    public OfferFileRepository(String filename){
        super(filename);
    }

    protected String convertObjectToString(Offer offer){
        if(offer==null){
            throw new IllegalArgumentException("Offer object cannot be null");
        }

        return offer.getId() + "," +
                offer.getMessage() + ","+
                offer.getOfferedPrice() + ","+
                offer.getTargetedProduct() + ","+
                offer.getSender() + ","+
                offer.getReceiver() + ","+
                offer.getStatus();
    }

    protected Offer createObjectFromString (String line){

        if(line==null || line.trim().isEmpty()){
            throw new IllegalArgumentException("Line to parse cannot be null or empty");
        }

        try {
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String message = parts[1];
            double offeredPrice = Double.parseDouble(parts[2]);
            int targetedProduct = Integer.parseInt(parts[3]);
            int sender = Integer.parseInt(parts[4]);
            int receiver = Integer.parseInt(parts[5]);

            Offer offer = new Offer(message, offeredPrice, targetedProduct, sender, receiver);
            offer.setId(id);
            return offer;
        }catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
            throw new IllegalArgumentException("Error parsing user data: " + line, e);
        }





    }
}
