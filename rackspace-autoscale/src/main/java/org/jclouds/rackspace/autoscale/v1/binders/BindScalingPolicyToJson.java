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
package org.jclouds.rackspace.autoscale.v1.binders;

import java.util.Map;

import org.jclouds.http.HttpRequest;
import org.jclouds.rackspace.autoscale.v1.domain.CreateScalingPolicy;
import org.jclouds.rackspace.autoscale.v1.internal.ParseHelper;
import org.jclouds.rest.MapBinder;
import org.jclouds.rest.binders.BindToJsonPayload;

import com.google.inject.Inject;

/**
 * Decouple building the json object from the domain objects structure by using the binder
 * @author Zack Shoylev
 */
public class BindScalingPolicyToJson implements MapBinder {

   private final BindToJsonPayload jsonBinder;

   @Inject
   private BindScalingPolicyToJson(BindToJsonPayload jsonBinder) {
      this.jsonBinder = jsonBinder;
   }

   @Override    
   public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams) {
      CreateScalingPolicy scalingPolicy = (CreateScalingPolicy) postParams.get("scalingPolicy");
      return jsonBinder.bindToRequest(request, ParseHelper.buildScalingPolicyMap(scalingPolicy));
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object toBind) {
      throw new IllegalStateException("Update policy is a POST operation");
   }
}
