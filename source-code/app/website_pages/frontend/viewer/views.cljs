
(ns app.website-pages.frontend.viewer.views
    (:require [app.common.frontend.api                 :as common]
              [app.components.frontend.api             :as components]
              [app.website-pages.frontend.viewer.boxes :as viewer.boxes]
              [elements.api                            :as elements]
              [layouts.surface-a.api                   :as surface-a]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :website-pages.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- page-overview
  []
  [:<> [viewer.boxes/page-data-box]
       [viewer.boxes/page-content-box]
       [viewer.boxes/page-more-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :website-pages.viewer])]
       (case current-view-id :overview [page-overview])))

(defn- body
  []
  [common/item-viewer-body :website-pages.viewer
                           {:item-element [view-selector]
                            :item-path    [:website-pages :viewer/viewed-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [page-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        page-name @(r/subscribe [:x.db/get-item [:website-pages :viewer/viewed-item :name]])
        edit-route (str "/@app-home/website-pages/"page-id"/edit")]
       [common/item-viewer-header :website-pages.viewer
                                  {:label       page-name
                                   :placeholder :unnamed-website-page
                                   :crumbs     [{:label :app-home      :route "/@app-home"}
                                                {:label :website-pages :route "/@app-home/website-pages"}
                                                {:label page-name :placeholder :unnamed-website-page}]
                                   :page-items [{:label :overview}]
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
