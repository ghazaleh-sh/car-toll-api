package ir.co.sadad.cartollapi.providers.naji;

import ir.co.sadad.cartollapi.dtos.ViolationAggregationData;
import ir.co.sadad.cartollapi.dtos.ViolationImageData;
import ir.co.sadad.cartollapi.dtos.ViolationListData;

/**
 * services for naji services
 * <pre>
 *     in this service , request must be encrypt
 * </pre>
 */
public interface SadadNajiServices {


    /**
     * services for get violation aggregation from naji servicess
     *
     * @param phoneNo phone no of owner of plate
     * @param plateNo plateNo
     * @param ssn     ssn of owner
     * @return violation aggregation
     */
    ViolationAggregationData getViolationAggregation(String phoneNo, String plateNo, String ssn);

    /**
     * services for getting violation list
     *
     * @param phoneNo phone no of owner of plate
     * @param plateNo plateNo
     * @param ssn     ssn of owner
     * @return violation list
     */
    ViolationListData getViolationList(String phoneNo, String plateNo, String ssn);


    /**
     * service for getting violation image based on serial id of violation
     *
     * @param phoneNo  phone number of user
     * @param plateNo  plate no
     * @param ssn      ssn
     * @param serialNo serial no of violation
     * @return image of violation
     */
    ViolationImageData getViolationImage(String phoneNo, String plateNo, String ssn, String serialNo);

}
