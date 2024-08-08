
package com.tasi.backend;

import spark.Spark;

public class Main {

    /** Controller to deal with all the requests. */
    private static final Controller controller = new Controller();

    public static void main(String[] args) {
        // Get results endpoint
        // Request and Answer are send to controller
        Spark.get("/crawl/:id", controller::getRequest);
        // Request search endpoint
        // Request and Answer are send to controller
        Spark.post("/crawl", controller::postRequest);
    }
}
