package ru.axothy.backdammon.gameservice.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class Dice {
    @Autowired
    private RestTemplate restTemplate;

    public static final String RANDOM_NUMBER_URL = "https://www.random.org/integers/?num=1&min=1&max=6&col=1&base=10&format=plain&rnd=new";
    public int roll() {
        String response = restTemplate.getForObject(RANDOM_NUMBER_URL, String.class);

        return Integer.parseInt(response.replaceAll("\\s", ""));
    }

}
