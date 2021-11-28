public class Tracinhos implements Cloneable
{
    private char texto [];

    public Tracinhos (int qtd) throws Exception 
    {   
        if (qtd <= 1)
            throw new Exception("Quatidade de tracinhos invalida!");

        this.texto = new char[qtd];

        for (byte i = 0; i < qtd; i++)
            this.texto[i] = '_';
    }

    public void revele (int posicao, char letra) throws Exception
    { 
        if (posicao < 0 || posicao >= this.texto.length)
            throw new Exception("Posicao para revelacao invalida!");

        this.texto[posicao] = letra;
    }

    public boolean isAindaComTracinhos ()
    { 
        boolean isThereTracinhos = false;

        for (byte i = 0; i < (byte)this.texto.length; i++) 
        {
            if (this.texto[i] == '_') 
            {
                isThereTracinhos = true;
                break;
            }
        }

        return isThereTracinhos;
    }

    @Override
    public String toString ()
    {
        String caracteres = "";
        for (byte i = 0; i < (byte)this.texto.length; i++) 
            caracteres += String.format("%c ", this.texto[i]);

        return caracteres;
    }

    @Override
    public boolean equals (Object obj)
    {
        if (this == obj) return true;

        if (obj == null) return false;

        if (obj.getClass() != Tracinhos.class) return false;

        Tracinhos trac = (Tracinhos)obj;
        if (this.texto != trac.texto) return false;

        return true;
    }

    @Override
    public int hashCode () 
    {
        int ret = 666;

        ret = 11 * ret + new char[this.texto.length].hashCode(); 
        
        if (ret < 0) ret = -ret;

        return ret;
    }

    // construtor de cópia
    public Tracinhos (Tracinhos t) throws Exception
    {
        if (t == null)
            throw new Exception("Modelo ausente");

        this.texto = new char[t.texto.length];

        for (short i = 0; i < t.texto.length; i++)
            this.texto[i] = t.texto[i];
    }

    @Override
    public Object clone ()
    {
        Tracinhos ret = null;

        try
        {
            ret = new Tracinhos(this);
        }
        catch (Exception erro)
        {} // ignorando Exception

        return ret;
    }
}
