package com.charles.productservice.controller;
 
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.charles.productservice.dto.ImageDTO;
import com.charles.productservice.dto.ProductDTO;
import com.charles.productservice.service.ImageService;
 
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	private ProductDTO product;
	
	public ImageController(ProductDTO p){
		product = p;
	}
	
	/*
	 * Returns all the images
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ImageDTO> findAll() {
		
		if (product != null)
			return imageService.findByProductId(product.getId());
		
		return imageService.findAll();
	}
	
	/*
	 * Returns a image by the id
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") Long id) {
		
		ImageDTO p = imageService.findByIdAndProductId(id, product.getId());
		
        if(p == null)
			return Response.status(HttpStatus.NOT_FOUND.value()).build(); 
		else return Response.status(HttpStatus.OK.value()).entity(p).build();
	}

	/*
	 * Save a image, if the image is invalid is returned the status NOT_FOUND
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response save(ImageDTO image) {
		
		image.setProduct(product);
		ImageDTO i = imageService.save(image);
		
		if (i == null)
			return Response.status(HttpStatus.NOT_FOUND.value()).build();
		return Response.status(HttpStatus.CREATED.value()).entity(i).build(); 
	}
	
	/*
	 * Update a image
	 */
	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, ImageDTO image) {
		
		image.setProduct(product);
		image.setId(id);
		
		if(imageService.findByIdAndProductId(id, product.getId()) != null
			&& imageService.update(image))
			return Response.status(HttpStatus.OK.value()).entity(image).build();

		return Response.status(HttpStatus.NOT_FOUND.value()).build();
	}
	
	/*
	 * Delete a image
	 */
	@DELETE
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") Long id) {
		
		ImageDTO i = imageService.findByIdAndProductId(id, product.getId());
		
		if (i != null && imageService.delete(i))
			return Response.status(HttpStatus.OK.value()).build();

		return Response.status(HttpStatus.NOT_FOUND.value()).build();
		
	}
}