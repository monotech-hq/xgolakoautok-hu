
(ns app.packages.frontend.viewer.views
    (:require [app.common.frontend.api            :as common]
              [app.components.frontend.api        :as components]
              [app.packages.frontend.viewer.boxes :as viewer.boxes]
              [elements.api                       :as elements]
              [layouts.surface-a.api              :as surface-a]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :packages.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- package-services
  []
  [:<> [viewer.boxes/package-services-box]])

(defn- package-products
  []
  [:<> [viewer.boxes/package-products-box]])

(defn- package-overview
  []
  [:<> [viewer.boxes/package-data-box]
       [viewer.boxes/package-thumbnail-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :packages.viewer])]
       (case current-view-id :overview [package-overview]
                             :products [package-products]
                             :services [package-services])))

(defn- body
  []
  [common/item-viewer-body :packages.viewer
                           {:item-element [view-selector]
                            :item-path    [:packages :viewer/viewed-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [package-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        package-name @(r/subscribe [:x.db/get-item [:packages :viewer/viewed-item :name]])
        edit-route    (str "/@app-home/packages/"package-id"/edit")]
       [common/item-viewer-header :packages.viewer
                                  {:label       package-name
                                   :placeholder :unnamed-package
                                   :crumbs     [{:label :app-home :route "/@app-home"}
                                                {:label :packages :route "/@app-home/packages"}
                                                {:label package-name :placeholder :unnamed-package}]
                                   :menu-items [{:label :overview}
                                                {:label :products}
                                                {:label :services}]
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
