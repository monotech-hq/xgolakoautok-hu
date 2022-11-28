
(ns app.price-quote-templates.frontend.viewer.views
    (:require [app.common.frontend.api                         :as common]
              [app.components.frontend.api                     :as components]
              [app.price-quote-templates.frontend.viewer.boxes :as viewer.boxes]
              [elements.api                                    :as elements]
              [layouts.surface-a.api                           :as surface-a]
              [re-frame.api                                    :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :price-quote-templates.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- template-overview
  []
  [:<> [viewer.boxes/template-data-box]
       [viewer.boxes/template-issuer-logo-box]
       [viewer.boxes/template-issuer-details-box]
       [viewer.boxes/template-body-box]
       [viewer.boxes/template-footer-box]
       [viewer.boxes/template-informations-box]
       [viewer.boxes/template-settings-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :price-quote-templates.viewer])]
       (case current-view-id :overview [template-overview])))

(defn- body
  []
  [common/item-viewer-body :price-quote-templates.viewer
                           {:item-element [view-selector]
                            :item-path    [:price-quote-templates :viewer/viewed-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [template-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        template-name @(r/subscribe [:x.db/get-item [:price-quote-templates :viewer/viewed-item :name]])
        edit-route     (str "/@app-home/price-quote-templates/"template-id"/edit")]
       [common/item-viewer-header :price-quote-templates.viewer
                                  {:label       template-name
                                   :placeholder :unnamed-price-quote-template
                                   :crumbs     [{:label :app-home              :route "/@app-home"}
                                                {:label :price-quote-templates :route "/@app-home/price-quote-templates"}
                                                {:label template-name :placeholder :unnamed-price-quote-template}]
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
