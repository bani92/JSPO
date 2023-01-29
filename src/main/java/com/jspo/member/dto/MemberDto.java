package com.jspo.member.dto;


import javax.validation.constraints.*;
import java.sql.Date;
import java.util.Objects;

public class MemberDto {

    private int id;
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 형식으로 작성해주세요. ex) jspo@jspo.com")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야 합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String pwd;
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    @NotNull(message = "생년월일을 입력해주세요.")
    @Past(message = "올바른 생년월일을 입력해주세요.")
    private Date birth;
    @NotBlank(message = "핸드폰번호를 입력해주세요.")
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "MemberDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", birth=" + birth +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDto memberDto = (MemberDto) o;
        return id == memberDto.id && Objects.equals(email, memberDto.email) && Objects.equals(pwd, memberDto.pwd) && Objects.equals(name, memberDto.name) && Objects.equals(birth, memberDto.birth) && Objects.equals(phone, memberDto.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, pwd, name, birth, phone);
    }

    public MemberDto(int id, String email, String pwd, String name, Date birth, String phone) {
        this.id = id;
        this.email = email;
        this.pwd = pwd;
        this.name = name;
        this.birth = birth;
        this.phone = phone;
    }

    public MemberDto() { }

    // DB조회용 user객체 싱글톤패턴으로 재사용
    private static final MemberDto instance = new MemberDto();

    public static MemberDto getInstance() {
        return instance;
    }

}
