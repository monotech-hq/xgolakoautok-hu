
(ns app.contents.frontend.viewer.views
    (:require [app.common.frontend.api               :as common]
              [app.components.frontend.api           :as components]
              [app.contents.frontend.handler.helpers :as handler.helpers]
              [app.contents.frontend.viewer.boxes    :as viewer.boxes]
              [elements.api                          :as elements]
              [layouts.surface-a.api                 :as surface-a]
              [re-frame.api                          :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-viewer-footer :contents.viewer {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- content-overview
  []
  [:<> [viewer.boxes/content-data-box]
       [viewer.boxes/content-content-box]
       [viewer.boxes/content-more-data-box]
       (str (uri.api/to-subdomain "http://bsa.com/sffs"))])
       ;;(str (re-matches #"^[a-z]{1,}\.([a-z]{1,})$" "a.ccc"))])
       ;(str (re-matches #"nov(ember)?" "november"))])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :contents.viewer])]
       (case current-view-id :overview [content-overview])))

(defn- body
  []
  [common/item-viewer-body :contents.viewer
                           {:item-element [view-selector]
                            :item-path    [:contents :viewer/viewed-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [content-id   @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        content-name @(r/subscribe [:x.db/get-item [:contents :viewer/viewed-item :name]])
        edit-route    (str "/@app-home/contents/"content-id"/edit")]
       [common/item-viewer-header :contents.viewer
                                  {:label       content-name
                                   :placeholder :unnamed-content
                                   :crumbs     [{:label :app-home :route "/@app-home"}
                                                {:label :contents :route "/@app-home/contents"}
                                                {:label content-name :placeholder :unnamed-content}]
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
