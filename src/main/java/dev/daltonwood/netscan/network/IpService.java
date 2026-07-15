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
import org.springframework.stereotype.Service;

@Component
@Service
public class IpService {

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

    public String getSubnetInfo(String cidrValue) {

        SubnetUtils block = new SubnetUtils(cidrValue);
        return block.getInfo().toString();
    }

    public Iterable<String> getIterableFromSubnet(String cidrValue) {

        SubnetUtils subnetUtils = new SubnetUtils(cidrValue);

        return subnetUtils.getInfo().iterableAddressStrings();

    }

}
