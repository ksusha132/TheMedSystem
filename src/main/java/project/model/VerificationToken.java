package project.model;

import java.util.Date;

import javax.persistence.*;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
public class VerificationToken {
	
	public static final int TOKEN_EXPIRATION_MIN = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", foreignKey = @ForeignKey(name = "FK_VERIFY_USER"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column
    private Date expiryDate;

    public VerificationToken() {}

    public VerificationToken(final String token) {
        this.token = token;
        this.expiryDate = DateUtils.addMinutes(new Date(), TOKEN_EXPIRATION_MIN);
    }

    public VerificationToken(final String token, final User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = DateUtils.addMinutes(new Date(), TOKEN_EXPIRATION_MIN);
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(final Date expiryDate) {
        this.expiryDate = expiryDate;
    }


    public void updateToken(final String token) {
        this.token = token;
        this.expiryDate = DateUtils.addMinutes(new Date(), TOKEN_EXPIRATION_MIN);
    }
}
