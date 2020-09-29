

import com.pricebasket.app.dal.InMemoryItemDAOImpl;
import com.pricebasket.app.dal.InMemoryOfferDaoImpl;
import com.pricebasket.app.model.BasketResult;
import com.pricebasket.app.service.BasketService;
import com.pricebasket.app.service.ItemService;
import com.pricebasket.app.service.OfferService;

import java.util.*;


public class Main {

    public static void main(String[] args) {
         //String basket = "PriceBasket Apples Milk Bread";
          String basket = "PriceBasket Apples Milk Soup Soup Soup Soup Bread Bread";


        String[] basketArray = basket.split(" ");
        String[] bItems = Arrays.copyOfRange(basketArray, 1, basketArray.length);
        BasketService basketService = new BasketService(new ItemService(new InMemoryItemDAOImpl()),
                new OfferService(new InMemoryOfferDaoImpl()));
        BasketResult basketResult = basketService.ProcessBasket(bItems);

        for (String ds : basketResult.getMessages()) {
            System.out.println(ds);
        }

    }
}
