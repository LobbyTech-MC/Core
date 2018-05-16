

package me.dablakbandit.core.zip.crypto.PBKDF2;



interface PRF
{
    public void init(byte[] P);

    public byte[] doFinal(byte[] M);

    public int getHLen();
}
