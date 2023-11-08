package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;
import com.amplifyframework.core.model.ModelIdentifier;

import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Product type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Products", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Task implements Model {
  public static final QueryField ID = field("Task", "id");
  public static final QueryField Title = field("Task", "title");
  public static final QueryField Body = field("Task", "body");
  public static final QueryField DATE_CREATED = field("Task", "dateCreated");
  public static final QueryField State = field("Task", "state");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String name;
  private final @ModelField(targetType="String") String description;
  private final @ModelField(targetType="AWSDateTime") Temporal.DateTime dateCreated;
  private final @ModelField(targetType="productCategoryEnum") ProductCategoryEnum productCategory;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
    private String title;
    private String body;
    private NameStep state;

    /** @deprecated This API is internal to Amplify and should not be used. */
  @Deprecated
   public String resolveIdentifier() {
    return id;
  }

  public String getId() {
      return id;
  }
  
  public String getName() {
      return name;
  }
  
  public String getDescription() {
      return description;
  }
  
  public Temporal.DateTime getDateCreated() {
      return dateCreated;
  }

  public ProductCategoryEnum getProductCategory() {
      return productCategory;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Task(String id, String name, String description, Temporal.DateTime dateCreated, ProductCategoryEnum productCategory) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.dateCreated = dateCreated;
    this.productCategory = productCategory;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Task product = (Task) obj;
      return ObjectsCompat.equals(getId(), product.getId()) &&
              ObjectsCompat.equals(getName(), product.getName()) &&
              ObjectsCompat.equals(getDescription(), product.getDescription()) &&
              ObjectsCompat.equals(getDateCreated(), product.getDateCreated()) &&
              ObjectsCompat.equals(getProductCategory(), product.getProductCategory()) &&
              ObjectsCompat.equals(getCreatedAt(), product.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), product.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getName())
      .append(getDescription())
      .append(getDateCreated())
      .append(getProductCategory())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Product {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("name=" + String.valueOf(getName()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("dateCreated=" + String.valueOf(getDateCreated()) + ", ")
      .append("productCategory=" + String.valueOf(getProductCategory()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static NameStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Task justId(String id) {
    return new Task(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      name,
      description,
      dateCreated,
      productCategory);
  }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public NameStep getState() {
        return state;
    }

    public void setState(NameStep state) {
        this.state = state;
    }

    public interface NameStep {
    BuildStep name(String name);

        String name();
    }
  

  public interface BuildStep {
    Task build();
    BuildStep id(String id);
    BuildStep description(String description);
    BuildStep dateCreated(Temporal.DateTime dateCreated);
    BuildStep productCategory(ProductCategoryEnum productCategory);
  }
  

  public static class Builder implements NameStep, BuildStep {
    private String id;
    private String name;
    private String description;
    private Temporal.DateTime dateCreated;
    private ProductCategoryEnum productCategory;
    public Builder() {
      
    }
    
    private Builder(String id, String name, String description, Temporal.DateTime dateCreated, ProductCategoryEnum productCategory) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.dateCreated = dateCreated;
      this.productCategory = productCategory;
    }
    
    @Override
     public Task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Task(
          id,
          name,
          description,
          dateCreated,
          productCategory);
    }
    
    @Override
     public BuildStep name(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }

      @Override
      public String name() {
          return null;
      }

      @Override
     public BuildStep description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
     public BuildStep dateCreated(Temporal.DateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }
    
    @Override
     public BuildStep productCategory(ProductCategoryEnum productCategory) {
        this.productCategory = productCategory;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String name, String description, Temporal.DateTime dateCreated, ProductCategoryEnum productCategory) {
      super(id, name, description, dateCreated, productCategory);
      Objects.requireNonNull(name);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder dateCreated(Temporal.DateTime dateCreated) {
      return (CopyOfBuilder) super.dateCreated(dateCreated);
    }
    
    @Override
     public CopyOfBuilder productCategory(ProductCategoryEnum productCategory) {
      return (CopyOfBuilder) super.productCategory(productCategory);
    }
  }
  

  public static class ProductIdentifier extends ModelIdentifier<Task> {
    private static final long serialVersionUID = 1L;
    public ProductIdentifier(String id) {
      super(id);
    }
  }
  
}
