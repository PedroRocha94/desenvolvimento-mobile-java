package com.ufc.navegacaoentretelas.model;

public class Pokemon {

    private static int contadorId = 0;

    private int id;
    private String nome;
    private String tipo;
    private String ataque;
    private String defesa;

    public Pokemon(String nome, String tipo, String ataque, String defesa) {

        this.id = contadorId++;

        this.nome = nome;
        this.tipo = tipo;
        this.ataque = ataque;
        this.defesa = defesa;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAtaque() {
        return ataque;
    }

    public void setAtaque(String ataque) {
        this.ataque = ataque;
    }

    public String getDefesa() { return defesa; }

    public void setDefesa(String defesa) {
        this.defesa = defesa;
    }

    @Override
    public String toString() {
        return nome;
    }
}
