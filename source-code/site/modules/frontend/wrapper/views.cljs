
(ns site.modules.frontend.wrapper.views
    (:require [re-frame.api                 :as r]
              [site.components.frontend.api :as components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-menu
  []
  [:div#sp-sidebar--menu-items
    [:a.sp-sidebar--menu-item.si-effect--underline
      {:href     "/my-first-page"
       :on-click #(r/dispatch [:site.components/hide-sidebar!])}
      "My first page"]])

(defn sidebar
  []
  [components/sidebar {:content #'sidebar-menu}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn company-name-and-slogan
  []
  (let [company-name   @(r/subscribe [:db/get-item [:site :website-config :company-name]])
        company-slogan @(r/subscribe [:db/get-item [:site :website-config :company-slogan]])]
       [:a {:href "/" :style {:text-decoration "none"}}
         [:div#sp-navbar--company-name-and-slogan
           [:div#sp-navbar--company-name   company-name]
           [:div#sp-navbar--company-slogan company-slogan]]]))

(defn header
  []
  [components/navbar {:logo       #'company-name-and-slogan
                      :menu-items [{:href "/my-first-page" :label "My first-page" :class :si-effect--underline}]
                      :on-menu    [:site.components/show-sidebar!]
                      :threshold  800}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn wrapper
  [ui-structure]
  [:div#sp
    [ui-structure]])
    ; [header]])
    ; [sidebar]])

(defn view
  [ui-structure]
  [wrapper ui-structure])
