package com.eustache.book_network.history;

import com.eustache.book_network.book.Book;
import com.eustache.book_network.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BookTransactionHistory extends BaseEntity {
  // @ManyToOne
  @Column(name = "user_id")
  private String userId;
  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;
  private boolean returned;
  private boolean returnApproved;
}
