
(ns app.clients.frontend.viewer.views
    (:require [app.clients.frontend.viewer.boxes :as viewer.boxes]
              [app.common.frontend.api           :as common]
              [app.components.frontend.api       :as components]
              [elements.api                      :as elements]
              [layouts.surface-a.api             :as surface-a]
              [re-frame.api                      :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :clients.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-overview
  []
  [:<> [viewer.boxes/client-basic-data-box]
       [viewer.boxes/client-company-data-box]
       [viewer.boxes/client-billing-data-box]
       [viewer.boxes/client-more-data-box]])

(defn- client-price-quotes
  [])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :clients.viewer])]
       (case current-view-id :overview     [client-overview]
                             :price-quotes [client-price-quotes])))

(defn- body
  []
  [common/item-viewer-body :clients.viewer
                           {:item-element [view-selector]
                            :item-path    [:clients :viewer/viewed-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [client-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        client-name @(r/subscribe [:clients.viewer/get-client-name])
        edit-route   (str "/@app-home/clients/"client-id"/edit")]
       [common/item-viewer-header :clients.viewer
                                  {:label       client-name
                                   :placeholder :unnamed-client
                                   :crumbs     [{:label :app-home :route "/@app-home"}
                                                {:label :clients  :route "/@app-home/clients"}
                                                {:label client-name :placeholder :unnamed-client}]
                                   :menu-items [{:label :overview}
                                                {:label :price-quotes :disabled? true}]
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
