package xyz.sinsong.core.properties;

import lombok.Data;

/**
 * @author Jin Huang
 * @date 2021/11/1 10:04
 */

public class ReceiverConfig {
    private ReceiverScanProperties receiverScanProperties;

    public ReceiverConfig(ReceiverScanProperties receiverScanProperties) {
        this.receiverScanProperties = receiverScanProperties;
    }
    public String getBasePackage(){
        return this.receiverScanProperties.getScanPackage();
    }
    public String getCommandProcessor(){
        return this.getCommandProcessor();
    }

}
