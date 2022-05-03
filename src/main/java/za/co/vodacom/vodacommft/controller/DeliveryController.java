package za.co.vodacom.vodacommft.controller;
/**
 * @author jan & modified by mz herbie on 2020/05/05
 * @package za.co.vodacom.vodacomMFT.dto
 */

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.vodacom.vodacommft.service.IFileDeliveryService;

import java.util.concurrent.ForkJoinPool;


@RestController
@RequestMapping("/v1/deliver")
@RequiredArgsConstructor
public class DeliveryController {

    final private IFileDeliveryService deliveryService;

    @GetMapping(value = "/{consumerCode}/{routeShortName}")
    public ResponseEntity updateTodo(@PathVariable String consumerCode, @PathVariable String routeShortName){

        try {
//            deliveryProcessing(consumerCode, routeShortName);
            System.out.println("Check >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
            return ResponseEntity.ok().body("Async service started running");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void deliveryProcessing(String consumerCode, String routeShortName) {
        deliveryService.deliveryProcessing(consumerCode, routeShortName);
    }
}
