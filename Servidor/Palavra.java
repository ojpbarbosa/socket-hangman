
public class Palavra extends Comunicado implements Comparable<Palavra>
{
    private String texto;

    public Palavra (String texto) throws Exception
    {
        if (texto == null || texto == " ")
            throw new Exception("Texto ausente");

        this.texto = texto;
    }

    public int getQuantidade (char letra) 
    {
        int qtdLetra = 0; 

        for (int i = 0; i < texto.length(); i++)
        {
            if (texto.charAt(i) == letra)
                qtdLetra ++;
        }

        return qtdLetra;
    }

    public int getPosicaoDaIezimaOcorrencia (int i, char letra) throws Exception
    {
        byte qtdAparicoes = 0;
        boolean achou = false;
        int posicao = -1;

        for (int j = 0; j < this.texto.length(); j++) {
            if (letra == texto.charAt(j)) {
                qtdAparicoes++;

                if (qtdAparicoes - 1 == i) { // Verifica se está na Iézima aparição da letra
                    posicao = j;
                    achou = true;
                    break;
                }
            }
        }
        
        if (!achou) {
            throw new Exception("Iézima aparição não encontrada");
        }

        return posicao;
    }

    public int getTamanho ()
    {
        return this.texto.length();
    }

    @Override
    public String toString ()
    {
        return this.texto;
    }

    @Override
    public boolean equals (Object obj)
    {
        if (this == obj) return true;

        if (obj == null) return false;

        if (obj.getClass() != Palavra.class) return false;

        Palavra palav = (Palavra)obj;
        if (this.texto != palav.texto) return false;

        return true;
    }

    @Override
    public int hashCode ()
    {
        int ret = 666;

        ret = 11 * ret + new String(this.texto).hashCode();

        if (ret < 0) ret = -ret;
        return ret;
    }

    @Override
    public int compareTo (Palavra palavra) 
    {
        if (this.texto.compareTo(palavra.texto) < 0) return -1;
        if (this.texto.compareTo(palavra.texto) > 0) return 1;

        return 0;
    }
}
