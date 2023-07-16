package me.moonways.bridgenet.rsi.endpoint;

import com.google.gson.Gson;

import java.util.ArrayList;

public class TestEndpointConfigCreate {

    public static void main(String[] args) {
        EndpointConfig endpointConfig = new EndpointConfig("%endpoint%.jar", new ArrayList<>());
        System.out.println(new Gson().toJson(endpointConfig));
    }
}
