
(ns app.clients.frontend.editor.views
    (:require [app.clients.frontend.editor.boxes :as editor.boxes]
              [app.common.frontend.api           :as common]
              [app.components.frontend.api       :as components]
              [elements.api                      :as elements]
              [layouts.surface-a.api             :as surface-a]
              [re-frame.api                      :as r]
              [x.locales.api                     :as x.locales]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- footer
  []
  [common/item-editor-footer :clients.editor {}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- client-data
  []
  [:<> [editor.boxes/client-basic-data-box]
       [editor.boxes/client-company-data-box]
       [editor.boxes/client-billing-data-box]
       [editor.boxes/client-more-data-box]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-selector
  []
  (let [current-view-id @(r/subscribe [:x.gestures/get-current-view-id :clients.editor])]
       (case current-view-id :data [client-data])))

(defn- body
  []
  (let [user-locale      @(r/subscribe [:x.user/get-user-locale])
        user-country-name (get-in x.locales/COUNTRY-LIST [user-locale :native])]
       [common/item-editor-body :clients.editor
                                {:form-element     [view-selector]
                                 :initial-item     {:country user-country-name}
                                 :item-path        [:clients :editor/edited-item]
                                 :suggestion-keys  [:city :zip-code :tags]
                                 :suggestions-path [:clients :editor/suggestions]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  []
  (let [client-name  @(r/subscribe [:clients.editor/get-client-name])
        client-id    @(r/subscribe [:x.router/get-current-route-path-param :item-id])
        client-route @(r/subscribe [:item-editor/get-item-route :clients.editor client-id])]
       [common/item-editor-header :clients.editor
                                  {:label       client-name
                                   :placeholder :unnamed-client
                                   :crumbs      [{:label :app-home   :route "/@app-home"}
                                                 {:label :clients    :route "/@app-home/clients"}
                                                 {:label client-name :route client-route :placeholder :unnamed-client}]
                                   :menu-items  [{:label :data :change-keys [:first-name :last-name :email-address
                                                                             :phone-number :country :zip-code :city
                                                                             :address :vat-no :description :colors
                                                                             :tags :company-name]}]}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword) surface-id
  [surface-id]
  [surface-a/layout surface-id
                    {:content [:<> [header]
                                   [body]
                                   [footer]]}])
