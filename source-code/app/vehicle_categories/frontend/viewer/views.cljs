
(ns app.vehicle-categories.frontend.viewer.views
    (:require [app.common.frontend.api                      :as common]
              [app.components.frontend.api                  :as components]
              [app.vehicle-categories.frontend.viewer.boxes :as viewer.boxes]
              [elements.api                                 :as elements]
              [layouts.surface-a.api                        :as surface-a]
              [re-frame.api                                 :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :vehicle-categories.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- category-vehicle-models
  []
  [:<> [viewer.boxes/category-vehicle-models-box]])

(defn- category-overview
  []
  [:<> [viewer.boxes/category-data-box]
       [viewer.boxes/category-thumbnail-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :vehicle-categories.viewer])]
       (case current-view-id :overview       [category-overview]
                             :vehicle-models [category-vehicle-models])))

(defn- body
  []
  [common/item-viewer-body :vehicle-categories.viewer
                           {:item-element [view-selector]
                            :item-path    [:vehicle-categories :viewer/viewed-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [category-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        category-name @(r/subscribe [:x.db/get-item [:vehicle-categories :viewer/viewed-item :name]])
        edit-route     (str "/@app-home/vehicle-categories/"category-id"/edit")]
       [common/item-viewer-header :vehicle-categories.viewer
                                  {:label       category-name
                                   :placeholder :unnamed-vehicle-category
                                   :crumbs     [{:label :app-home           :route "/@app-home"}
                                                {:label :vehicle-categories :route "/@app-home/vehicle-categories"}
                                                {:label category-name :placeholder :unnamed-vehicle-category}]
                                   :menu-items [{:label :overview}
                                                {:label :vehicle-models}]
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
