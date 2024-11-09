package hansanhha.records;

public record User(String id, String password, String nickname) {

    // compile error
    // Canonical Constructor parameters name  must be same as record component names
//    public User(String userId, String userPassword, String userNickname) {
//        this.id = userId;
//        this.password = password;
//        this.nickname = nickname;
//    }

    // declare canonical constructor
//    public User(String id, String password, String nickname) {
//        this.id = id;
//        this.password = password;
//        this.nickname = nickname;
//    }

    // this compact constructor is equivalent to the above constructor
    public User {
        id = id;
        password = password;
        nickname = nickname;
    }

    // non-canonical constructor
    public User(String id, String password, String nickname, String email, String phoneNumber) {
        this(id, password, nickname + email + phoneNumber);
    }

    // compile error
    // cannot contain an assignment to component field of the record class
//    public User {
//        this.id = id;
//        this.password = password;
//        this.nickname = nickname;
//    }
}
