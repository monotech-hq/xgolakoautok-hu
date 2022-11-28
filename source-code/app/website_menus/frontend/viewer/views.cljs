
(ns app.website-menus.frontend.viewer.views
    (:require [app.common.frontend.api                 :as common]
              [app.components.frontend.api             :as components]
              [app.website-menus.frontend.viewer.boxes :as viewer.boxes]
              [elements.api                            :as elements]
              [layouts.surface-a.api                   :as surface-a]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :website-menus.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- menu-overview
  []
  [:<> [viewer.boxes/menu-menu-items-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :website-menus.viewer])]
       (case current-view-id :overview [menu-overview])))

(defn- body
  []
  [common/item-viewer-body :website-menus.viewer
                           {:item-element [view-selector]
                            :item-path    [:website-menus :viewer/viewed-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [menu-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        menu-name @(r/subscribe [:x.db/get-item [:website-menus :viewer/viewed-item :name]])
        edit-route (str "/@app-home/website-menus/"menu-id"/edit")]
       [common/item-viewer-header :website-menus.viewer
                                  {:label       menu-name
                                   :placeholder :unnamed-website-menu
                                   :crumbs     [{:label :app-home      :route "/@app-home"}
                                                {:label :website-menus :route "/@app-home/website-menus"}
                                                {:label menu-name :placeholder :unnamed-website-menu}]
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
