package br.com.serverest.config;

public final class Config {
    private Config() {}


    public static final String BASE_URL = System.getProperty("baseUrl", "https://serverest.dev");
}
