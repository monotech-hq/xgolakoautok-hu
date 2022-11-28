
(ns app.services.frontend.viewer.views
    (:require [app.common.frontend.api            :as common]
              [app.components.frontend.api        :as components]
              [app.services.frontend.viewer.boxes :as viewer.boxes]
              [elements.api                       :as elements]
              [layouts.surface-a.api              :as surface-a]
              [re-frame.api                       :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :services.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- service-overview
  []
  [:<> [viewer.boxes/service-data-box]
       [viewer.boxes/service-thumbnail-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :services.viewer])]
       (case current-view-id :overview [service-overview])))

(defn- body
  []
  [common/item-viewer-body :services.viewer
                           {:item-element [view-selector]
                            :item-path    [:services :viewer/viewed-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [service-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        service-name @(r/subscribe [:x.db/get-item [:services :viewer/viewed-item :name]])
        edit-route    (str "/@app-home/services/"service-id"/edit")]
       [common/item-viewer-header :services.viewer
                                  {:label       service-name
                                   :placeholder :unnamed-service
                                   :crumbs     [{:label :app-home :route "/@app-home"}
                                                {:label :services :route "/@app-home/services"}
                                                {:label service-name :placeholder :unnamed-service}]
                                   :menu-items [{:label :overview}]
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
