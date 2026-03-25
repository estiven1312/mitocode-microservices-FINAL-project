package pe.com.peruapps.contractsmicroservice.domain.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Id<T> {
  private final T value;

  public Id(T value) {
    this.value = value;
  }

  public static class ThirdPartyId extends Id<UUID> {
    public ThirdPartyId(UUID value) {
      super(value);
    }
  }

  public static class OrganizationalEntityId extends Id<UUID> {
    public OrganizationalEntityId(UUID value) {
      super(value);
    }
  }

  public static class UserId extends Id<UUID> {
    public UserId(UUID value) {
      super(value);
    }
  }
}
