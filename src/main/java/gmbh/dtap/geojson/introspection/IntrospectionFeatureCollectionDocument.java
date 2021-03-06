/*
 * Copyright 2019 DTAP GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gmbh.dtap.geojson.introspection;

import gmbh.dtap.geojson.document.FeatureCollectionDocument;

import java.util.List;

/**
 * Default implementation of a {@link FeatureCollectionDocument}.
 *
 * @since 0.4.0
 */
public class IntrospectionFeatureCollectionDocument implements FeatureCollectionDocument {

   private final List<Object> features;

   /**
    * Constructor
    *
    * @param features the features, may be empty but not <tt>null</tt>
    * @since 0.4.0
    */
   IntrospectionFeatureCollectionDocument(List<Object> features) {
      this.features = features;
   }

   /**
    * {@inheritDoc}
    *
    * @since 0.4.0
    */
   @Override
   public List<Object> getFeatures() {
      return features;
   }
}
