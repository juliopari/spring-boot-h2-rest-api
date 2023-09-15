package com.globallogic.ejercicio.dtos;

import java.util.Date;

public class ErrorMessage {
  private String message;
  private Date timestamp;
  private Integer codigo;
  private String detail;

  public ErrorMessage(Date timestamp, Integer codigo, String detail) {
    super();
    this.timestamp = timestamp;
    this.codigo = codigo;
    this.detail = detail;
  }

  public ErrorMessage(String message) {
    this.message = message;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public Integer getCodigo() {
    return codigo;
  }

  public void setCodigo(Integer codigo) {
    this.codigo = codigo;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
