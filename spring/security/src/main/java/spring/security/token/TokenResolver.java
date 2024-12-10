package spring.security.token;

public interface TokenResolver<Token, Payload> {

    Payload resolveToken(Token t);

    boolean isValid(Token t);

}
