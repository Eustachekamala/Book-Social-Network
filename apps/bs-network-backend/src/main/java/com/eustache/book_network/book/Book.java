package com.eustache.book_network.book;

import com.eustache.book_network.common.BaseEntity;
import com.eustache.book_network.feedback.Feedback;
import com.eustache.book_network.history.BookTransactionHistory;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book extends BaseEntity {
  private String title;
  private String authorName;
  private String isbn;
  private String synopsis;
  private String bookCover;
  private boolean archived;
  private boolean shareable;

  // @ManyToOne
  // @JoinColumn(name = "owner_id")
  // private User owner;
  @OneToMany(mappedBy = "book")
  private List<Feedback> feedbacks;
  @OneToMany(mappedBy = "book")
  private List<BookTransactionHistory> histories;

  @Transient
  public double getRate() {
    if (feedbacks == null || feedbacks.isEmpty()) {
      return 0.0;
    }
    var rate = this.feedbacks.stream()
      .mapToDouble(Feedback::getNote)
      .average()
      .orElse(0.0);
    double roundedRate = Math.round(rate * 10.0) / 10.0;

    // Return 4.0 if roundedRate is less than 4.5, otherwise return 4.5
    return roundedRate;
  }
}
