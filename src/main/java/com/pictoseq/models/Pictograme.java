package com.pictoseq.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pictograme implements Serializable {
    public final String _id;
    public final String keyword;
    public final LinkedList<String> categories;
    @JsonIgnore
    private transient ImageView imageView;

    public Pictograme() {
        this._id = null;
        this.keyword = null;
        this.categories = null;
        this.imageView = null;
    }

    @JsonCreator
    public Pictograme(@JsonProperty("_id") String _id,
                      @JsonProperty("keywords") JsonNode keywords,
                      @JsonProperty("categories") LinkedList<String> categories) {
        this._id = _id;
        if (keywords != null && keywords.isArray() && keywords.size() > 0) {
            JsonNode firstKeywordNode = keywords.get(0);
            if (firstKeywordNode.has("keyword"))
                this.keyword = firstKeywordNode.get("keyword").asText();
            else this.keyword = null;
        } else this.keyword = null;
        this.categories = categories;
    }

    public Pictograme(Pictograme pictograme) {
        this._id = pictograme._id;
        this.keyword = pictograme.keyword;
        this.categories = new LinkedList<>(pictograme.categories);
        if (pictograme.imageView == null)
            this.imageView = null;
        else this.imageView = pictograme.getImageViewCopy();
    }

    public String get_id() {
        return _id;
    }

    public String getKeyword() {
        return keyword;
    }

    public LinkedList<String> getCategories() {
        return categories;
    }

    public boolean categoryContains(String category) {
        assert categories != null;
        return categories.contains(category);
    }

    @Override
    public String toString() {
        return "Pictograme{" +
                "_id='" + _id + '\'' +
                ", keyword='" + keyword + '\'' +
                ", categories=" + categories +
                '}';
    }

    public HttpResponse<InputStream> render(HttpClient client) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.arasaac.org/api/pictograms/" + this._id + "?download=false"))
                .GET()
                .setHeader("accept", "image/png")
                .build();

        try {
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            Image image = new Image(response.body());
            imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            return response;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ImageView getImageViewCopy() {
        ImageView imageViewCopy = new ImageView(imageView.getImage());
        imageViewCopy.setFitHeight(100);
        imageViewCopy.setFitWidth(100);
        return imageViewCopy;
    }

    public Node getImageView() {
        return imageView;
    }
}


