/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.openstack.glance.v2_0.functions.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.openstack.glance.v2_0.options.ListImageOptions.Builder.marker;

import java.beans.ConstructorProperties;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.IterableWithMarkers;
import org.jclouds.collect.internal.Arg0ToPagedIterable;
import org.jclouds.http.functions.ParseJson;
import org.jclouds.json.Json;
import org.jclouds.openstack.glance.v2_0.GlanceApi;
import org.jclouds.openstack.glance.v2_0.domain.Image;
import org.jclouds.openstack.glance.v2_0.features.ImageApi;
import org.jclouds.openstack.glance.v2_0.functions.internal.ParseImages.Images;
import org.jclouds.openstack.v2_0.domain.PaginatedCollection;
import org.jclouds.openstack.v2_0.domain.Link;
import org.jclouds.openstack.v2_0.options.PaginationOptions;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.inject.TypeLiteral;

/**
 * @author Ignacio Mulas
 */
@Beta
@Singleton
public class ParseImages extends ParseJson<Images> {
   static class Images extends PaginatedCollection<Image> {

      @ConstructorProperties({ "images", "images_links" })
      protected Images(Iterable<Image> images, Iterable<Link> images_links) {
         super(images, images_links);
      }

   }

   @Inject
   public ParseImages(Json json) {
      super(json, TypeLiteral.get(Images.class));
   }

   public static class ToPagedIterable extends Arg0ToPagedIterable.FromCaller<Image, ToPagedIterable> {

      private final GlanceApi api;

      @Inject
      protected ToPagedIterable(GlanceApi api) {
         this.api = checkNotNull(api, "api");
      }

      @Override
      protected Function<Object, IterableWithMarker<Image>> markerToNextForArg0(Optional<Object> arg0) {
         String zone = arg0.isPresent() ? arg0.get().toString() : null;
         final ImageApi imageApi = api.getImageApiForZone(zone);
         return new Function<Object, IterableWithMarker<Image>>() {

            @SuppressWarnings("unchecked")
            @Override
            public IterableWithMarker<Image> apply(Object input) {
               PaginationOptions paginationOptions = PaginationOptions.class.cast(input);
               Collection<String> markers = paginationOptions.buildQueryParameters().get("marker");

               if (!markers.isEmpty()) {
                  return IterableWithMarker.class.cast(imageApi.list(marker(Iterables.get(markers, 0))));
               } else {
                  return IterableWithMarkers.EMPTY;
               }
            }

            @Override
            public String toString() {
               return "list()";
            }
         };
      }

   }

}
