package pl.coderslab.chirper.payload;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import pl.coderslab.chirper.entity.User;
import pl.coderslab.chirper.restController.UserController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, Resource<User>> {
    @Override
    public Resource<User> toResource(User user) {
        return new Resource<User>(
                user, linkTo(methodOn(UserController.class).getOne(user.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).getAll()).withRel("users"));
    }
}
