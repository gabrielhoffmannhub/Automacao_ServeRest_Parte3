package br.com.serverest.models;



public record Usuario(String nome, String email, String password, String administrador) {

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAdministrador() {
        return administrador;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String nome = "QA Teste";
        private String email;
        private String password = "123456";
        private String administrador = "true";

        public Builder nome(String valor) {
            nome = valor;
            return this;
        }

        public Builder email(String valor) {
            email = valor;
            return this;
        }

        public Builder password(String valor) {
            password = valor;
            return this;
        }

        public Builder administrador(String valor) {
            administrador = valor;
            return this;
        }

        public Usuario build() {
            return new Usuario(nome, email, password, administrador);
        }
    }
}
