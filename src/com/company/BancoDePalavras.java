package com.company;

public class BancoDePalavras extends Comunicado
{
    private static String[] palavras = 
    {
            "ABIN",
            "AIRBUS",
            "AMARELO",
            "ANEL",
            "APPLE",
            "ARGENTINA",
            "AVIÃO",
            "AZUL",
            "BARCO",
            "BINÓCULO",
            "BIOLOGIA",
            "BITCOIN",
            "BOEING",
            "BOLA",
            "BOMBARDIER",
            "BRUXA",
            "BULE",
            "CAIXA",
            "CALCULADORA",
            "CAPOEIRA",
            "CARDIOLOGIA",
            "CHAPÉU",
            "CHICLETE",
            "CHINA",
            "CHOCOLATE",
            "CHUPETA",
            "CIA",
            "CINTO",
            "COLHER",
            "COMBUSTÍVEL",
            "CONJUNTIVITE",
            "COPO",
            "COTIL",
            "COUTCA",
            "DANÇARINO",
            "ELON",
            "EMBRAER",
            "ENVELOPE",
            "ENXADA",
            "FORCA",
            "FORD",
            "GARFO",
            "GEOGRAFIA",
            "GMAIL",
            "GOIABA",
            "GOOGLE",
            "HARPA",
            "HETEROSSEXUAL",
            "HISTÓRIA",
            "HOMEM",
            "HOMOSSEXUAL",
            "HULK",
            "INSTAGRAM",
            "JABUTICABA",
            "JACA",
            "JEFF",
            "KGB",
            "LUA",
            "MAMADEIRA",
            "MARK",
            "MATEMÁTICA",
            "MICKEY",
            "MICROSOFT",
            "MINECRAFT",
            "MINNIE",
            "MIT",
            "MLB",
            "NASA",
            "NBA",
            "NFL",
            "NHL",
            "NOBEL",
            "OFTALMOLOGIA",
            "ÔNIBUS",
            "OSCAR",
            "OVNI",
            "OXIGÊNIO",
            "PÁ",
            "PEDIATRIA",
            "PEN",
            "PICARETA",
            "PULSEIRA",
            "QUEIJO",
            "RÚSSIA",
            "SHAKESPEARE",
            "STEVE",
            "SUCRILHOS",
            "TECLADO",
            "TOYOTA",
            "TRATOR",
            "UFMG",
            "UNICAMP",
            "USP",
            "VATICANO",
            "VERDE",
            "VERMELHO",
            "VIA",
            "WALT",
            "WORD",
            "XADREZ",
    };

    public static Palavra getPalavraSorteada ()
    {
        Palavra palavra = null;

        try
        {
            palavra =
            new Palavra (BancoDePalavras.palavras[
            (int)(Math.random() * BancoDePalavras.palavras.length)]);
        }
        catch (Exception e)
        {}

        return palavra;
    }
}