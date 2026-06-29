package dev.daltonwood.netscan.network;

/*
*
*
*
*
*
*/

import org.apache.commons.net.util.SubnetUtils;
import org.springframework.stereotype.Component;

@Component
public class SubnetService {

    public boolean isValidCidr(String userInput) {

        if (userInput == null || userInput.isBlank()) {
            return false;
        }

        try {
            new SubnetUtils(userInput);
            return true;

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getSubnetInfo(String userInput) {

        SubnetUtils block = new SubnetUtils(userInput);
        return block.toString();
    }

}
