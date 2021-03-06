package com.bd.gateway.resolvers;

import com.bd.gateway.clients.AuthorClient;
import com.bd.gateway.clients.PostClient;
import com.bd.gateway.clients.RpcGraphqlConverter;
import com.bd.gateway.inputs.AddAuthorInput;
import com.bd.gateway.inputs.AddPostInput;
import com.bd.gateway.types.AddPostOutput;
import com.bd.gateway.types.Author;
import com.bd.gateway.types.Post;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import lombok.AllArgsConstructor;
import net.badata.protobuf.converter.Converter;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;
import sample.PostProto;
import sample.author.AuthorProto;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Component
public class Mutation implements GraphQLMutationResolver {

    private final PostClient postClient;

    private final AuthorClient authorClient;

    public CompletableFuture<AddPostOutput> addPost(AddPostInput data){
        return RpcGraphqlConverter.lazyTransform(postClient.addPost(Converter.create().toProtobuf(PostProto.AddPostRequest.class,data)), in -> {
            Post post = Converter.create().toDomain(Post.class, in);
            AddPostOutput output = new AddPostOutput();
            output.setMessage("post created");
            output.setResult(post);
            return output;
        });
    }

    public Author addAuthor(AddAuthorInput input){
        AuthorProto.Author author = authorClient.addAuthor(input.getName());
        Author authorBean = new Author();
        authorBean.setId(author.getId());
        authorBean.setName(author.getName());
        return authorBean;
    }

//    public AddPostOutput addPost(AddPostInput data){
//        PostProto.Post in = postClient.addPost(Converter.create().toProtobuf(PostProto.addPostRequest.class,data));
//        Post post = Converter.create().toDomain(Post.class, in);
//        AddPostOutput output = new AddPostOutput();
//        output.setMessage("post created");
//        output.setResult(post);
//        return output;
//    }

}
