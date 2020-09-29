package com.pricebasket.app.service;





import com.pricebasket.app.model.Item;
import com.pricebasket.app.model.MultiItemDiscOffer;
import com.pricebasket.app.model.SingleItemDiscOffer;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketService {

    private ItemService itemService;
    private OfferService offerService;
    private Map<String, Integer> shoppingBasket = new HashMap<>();
    static List<String> msgList = new ArrayList<>();

    public BasketService(ItemService itemService, OfferService offerService){
        this.itemService = itemService;
        this.offerService = offerService;
    }

    public List<String> ProcessBasket(String[] bItems){

        //Get Single Item Offers
        List<SingleItemDiscOffer> singleItemDiscs = offerService.getSinleItemOffers();

        //Get Multi Item Offers
        List<MultiItemDiscOffer> multiItemDiscs = offerService.getMultiItemOffers();



        double subTotal = 0.0;
        double total = 0.0;
        for (String bt : bItems) {
            shoppingBasket.merge(bt, 1, Integer::sum);
            Item curItem = itemService.getItem(bt);
            double disc;
            double itemPrice = curItem.getPrice();
            subTotal += itemPrice;
            boolean singleDiscExists = singleItemDiscs.stream()
                    .anyMatch(t -> t.getItemName().equals(bt));
            if (singleDiscExists) {
                disc = singleItemDiscs.stream()
                        .filter(t -> t.getItemName().equals(bt)).findFirst().get().getDisc();
                double discPrice = itemPrice * (disc / 100);
                itemPrice = itemPrice - discPrice;
                DecimalFormat format = new DecimalFormat("0.#");
                String discString = format.format(disc);
                String discPriceStr = format.format(discPrice * 100);

                msgList.add(String.format("%s  %s%% off:  -%sp", bt, discString, discPriceStr));
            }

            total += itemPrice;

        }

        for (Map.Entry<String, Integer> entry : shoppingBasket.entrySet()) {
            boolean multiBuyDiscExists = multiItemDiscs.stream()
                    .anyMatch(t -> t.getOfferItemName().equals(entry.getKey()));

            if (multiBuyDiscExists) {
                String offerSourceItem = multiItemDiscs.stream()
                        .filter(t -> t.getOfferItemName().equals(entry.getKey())).findFirst().get().getSouceitemName();

                boolean offerSourceInBasket = shoppingBasket.containsKey(offerSourceItem);
                if (offerSourceInBasket) {
                    int offerSourceQuantity = multiItemDiscs.stream()
                            .filter(t -> t.getOfferItemName().equals(entry.getKey())).findFirst().get().getSrcQuantity();
                    double disc = multiItemDiscs.stream()
                            .filter(t -> t.getOfferItemName().equals(entry.getKey())).findFirst().get().getDisc();
                    int srcItemsInbasket = shoppingBasket.get(offerSourceItem);
                    int itemQunatity = entry.getValue();
                    for(int i=srcItemsInbasket ;(i > 1 && itemQunatity >0);i= srcItemsInbasket - offerSourceQuantity) {
                        Item curItem = itemService.getItem(entry.getKey());
                        double itemPrice = curItem.getPrice();
                        double discPrice = itemPrice * (disc / 100);

                        total -= discPrice;
                        itemQunatity--;
                        DecimalFormat format = new DecimalFormat("0.#");
                        String discString = format.format(disc);
                        msgList.add(String.format("Buy %d %s get %s  %s%%  Off", offerSourceQuantity, offerSourceItem, entry.getKey(), discString));
                    }

                }
            }
        }


        msgList.add(0, String.format("Subtotal: %.2f", subTotal));

        msgList.add(String.format("Total: %.2f", total));
        return msgList;
    }
}
