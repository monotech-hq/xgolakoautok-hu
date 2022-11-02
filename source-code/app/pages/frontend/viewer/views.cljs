
(ns app.pages.frontend.viewer.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [engines.item-lister.api :as item-lister]
              [engines.item-viewer.api :as item-viewer]
              [layouts.surface-a.api   :as surface-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- page-overview
  [])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [current-view-id @(r/subscribe [:gestures/get-current-view-id :pages.viewer])]
       (case current-view-id :overview [page-overview])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-bar
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :pages.viewer])]
       [common/item-viewer-menu-bar :pages.viewer
                                    {:disabled?  viewer-disabled?
                                     :menu-items [{:label :overview}]}]))

(defn- controls
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :pages.viewer])
        page-id          @(r/subscribe [:router/get-current-route-path-param :item-id])
        edit-item-uri     (str "/@app-home/pages/"page-id"/edit")]
       [common/item-viewer-controls :pages.viewer
                                    {:disabled?     viewer-disabled?
                                     :edit-item-uri edit-item-uri}]))

(defn- breadcrumbs
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :pages.viewer])
        page-name        @(r/subscribe [:db/get-item [:pages :viewer/viewed-item :name]])]
       [common/surface-breadcrumbs :pages.viewer/view
                                   {:crumbs [{:label :app-home :route "/@app-home"}
                                             {:label :pages    :route "/@app-home/pages"}
                                             {:label page-name :placeholder :unnamed-page}]
                                    :disabled? viewer-disabled?}]))

(defn- label
  []
  (let [viewer-disabled? @(r/subscribe [:item-viewer/viewer-disabled? :pages.viewer])
        page-name        @(r/subscribe [:db/get-item [:pages :viewer/viewed-item :name]])]
       [common/surface-label :pages.viewer/view
                             {:disabled?   viewer-disabled?
                              :label       page-name
                              :placeholder :unnamed-page}]))

(defn- header
  []
  [:<> [:div {:style {:display "flex" :justify-content "space-between" :flex-wrap "wrap" :grid-row-gap "48px"}}
             [:div [label]
                   [breadcrumbs]]
             [:div [controls]]]
       [elements/horizontal-separator {:size :xxl}]
       [menu-bar]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [header]
       [body]])

(defn- page-viewer
  []
  [item-viewer/body :pages.viewer
                    {:auto-title?   true
                     :error-element [common/error-content {:error :the-item-you-opened-may-be-broken}]
                     :ghost-element #'common/item-viewer-ghost-element
                     :item-element  #'view-structure
                     :item-path     [:pages :viewer/viewed-item]
                     :label-key     :name}])

(defn view
  [surface-id]
  [surface-a/layout surface-id
                    {:page #'page-viewer}])
