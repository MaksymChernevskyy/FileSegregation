package com.segregator.entity;


import java.time.LocalDateTime;
import java.util.Objects;

public class FileParameters {
    private String name;
    private Format format;
    private LocalDateTime timeCreated;
    private Catalogs catalogs;

    public FileParameters() {
    }

    public FileParameters(String name, Format format, LocalDateTime timeCreated, Catalogs catalogs) {
        this.name = name;
        this.format = format;
        this.timeCreated = timeCreated;
        this.catalogs = catalogs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Catalogs getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(Catalogs catalogs) {
        this.catalogs = catalogs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileParameters that = (FileParameters) o;
        return Objects.equals(name, that.name) &&
                format == that.format &&
                Objects.equals(timeCreated, that.timeCreated) &&
                catalogs == that.catalogs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, format, timeCreated, catalogs);
    }
}
