
(ns app.rental-vehicles.frontend.viewer.views
    (:require [app.common.frontend.api                   :as common]
              [app.components.frontend.api               :as components]
              [app.rental-vehicles.frontend.viewer.boxes :as viewer.boxes]
              [elements.api                              :as elements]
              [layouts.surface-a.api                     :as surface-a]
              [re-frame.api                              :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :rental-vehicles.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vehicle-content
  []
  [:<> [viewer.boxes/vehicle-description-box]])

(defn- vehicle-images
  []
  [:<> [viewer.boxes/vehicle-images-box]])

(defn- vehicle-thumbnail
  []
  [:<> [viewer.boxes/vehicle-thumbnail-box]])

(defn- vehicle-overview
  []
  [:<> [viewer.boxes/vehicle-basic-data-box]
       [viewer.boxes/vehicle-technical-data-box]
       [viewer.boxes/vehicle-more-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :rental-vehicles.viewer])]
       (case current-view-id :overview  [vehicle-overview]
                             :thumbnail [vehicle-thumbnail]
                             :images    [vehicle-images]
                             :content   [vehicle-content])))

(defn- body
  []
  [common/item-viewer-body :rental-vehicles.viewer
                           {:item-element [view-selector]
                            :item-path    [:rental-vehicles :viewer/viewed-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [vehicle-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        vehicle-name @(r/subscribe [:x.db/get-item [:rental-vehicles :viewer/viewed-item :name]])
        edit-route    (str "/@app-home/rental-vehicles/"vehicle-id"/edit")]
       [common/item-viewer-header :rental-vehicles.viewer
                                  {:label       vehicle-name
                                   :placeholder :unnamed-rental-vehicle
                                   :crumbs     [{:label :app-home        :route "/@app-home"}
                                                {:label :rental-vehicles :route "/@app-home/rental-vehicles"}
                                                {:label vehicle-name :placeholder :unnamed-rental-vehicle}]
                                   :menu-items [{:label :overview}
                                                {:label :thumbnail}
                                                {:label :images}
                                                {:label :content}]
                                   :on-edit    [:x.router/go-to! edit-route]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
