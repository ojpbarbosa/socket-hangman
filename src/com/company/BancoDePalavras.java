package com.company;

public class BancoDePalavras extends Comunicado
{
    private static String[] palavras = 
    {
        "MALICIOSO",
        "ANDORINHA",
        "AMARELO",
        "MACAQUICE",
        "PEIXE-ESPADA",
        "ARGENTINA",
        "DENTIÇÃO",
        "OTORRINOLARINGOLOGISTA",
        "ANTIGUIDADES",
        "BINÓCULO",
        "BIOLOGIA",
        "BITCOIN",
        "VIDEIRA",
        "EMPACOTADO",
        "BOMBARDIER",
        "HOMEM-ARANHA",
        "DESCONGESTIONADO",
        "CHUCRUTE",
        "CALCULADORA",
        "CAPOEIRA",
        "CARDIOLOGIA",
        "BOMBORDO",
        "CHICLETE",
        "EXTRAORDINÁRIO",
        "CHOCOLATE",
        "CHUPETA",
        "CONSTITUCIONALIZAÇÃO",
        "SIGNIFICAMENTE",
        "PRIMORDIAL",
        "COMBUSTÍVEL",
        "CONJUNTIVITE",
        "INVALIDADE",
        "TRANSDICIPLINARIDADE",
        "COTUCA",
        "DANÇARINO",
        "AQUOSO",
        "POLICHINELO",
        "ENVELOPE",
        "DECONGEX",
        "APARÊNCIA",
        "MULTIDICIPLINAR",
        "INTERDICIPLINAR",
        "GEOGRAFIA",
        "CARAPAÇA",
        "VERDE-AZULADO",
        "ESTIBORDO",
        "DESENVOLVIMENTO",
        "HETEROSSEXUAL",
        "HISTÓRIA",
        "ADOLESCÊNCIA",
        "HOMOSSEXUAL",
        "MULTIDIMENsIONAL",
        "INSTAGRAM",
        "JABUTICABA",
        "DESACORDADO",
        "INTERCONFESSIONALISMO",
        "MICROCEFALIA",
        "HIPOTETICAMENTE",
        "INTESTINO",
        "MILAGROSAMENTE",
        "MATEMÁTICA",
        "MICKEY",
        "MICROSOFT",
        "RONRONANDO",
        "MINNIE",
        "HETEROIDENTIFICAÇÃO",
        "MICROPIGMENTAÇÃO",
        "NASA",
        "COMUNINDADE",
        "CALIGRAFIA",
        "TRAQUÉIA",
        "NEBULIZADOR",
        "OFTALMOLOGIA",
        "TERMOSFERA",
        "SUBSOLO",
        "HIPOPÓTAMO",
        "OXIGÊNIO",
        "PNEUMONIA",
        "PEDIATRIA",
        "PNEUMOULTRAMICROSCÓPIOSSILICOVULCANOCONIÓTICO",
        "PICARETA",
        "PULSEIRA",
        "CARBENICILINA",
        "ANTIBIÓTICO",
        "SHAKESPEARE",
        "RADIOLOGISTA",
        "SUCRILHOS",
        "TECLADO",
        "ASPIRADOR",
        "FISIOLOGIA",
        "CINEMATOGRÁFICO",
        "UNICAMP",
        "INCETICIDA",
        "VATICANO",
        "ANTICONSTITUCIONALÍSTICO",
        "VERMELHO",
        "INCOMPETENTE",
        "DESCOLORANTE",
        "PROGRAMADOR",
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