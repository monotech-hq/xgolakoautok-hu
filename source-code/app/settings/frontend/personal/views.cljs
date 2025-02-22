
(ns app.settings.frontend.personal.views
    (:require [css.api      :as css]
              [elements.api :as elements]
              [re-frame.api :as r]))

;; -- Body components ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn change-profile-picture-button
  []
  [elements/button ::change-profile-picture-button
                   {:label     :change-profile-picture!
                    :preset    :default-button
                    :font-size :xs}])

(defn user-profile-picture
  []
  (let [user-profile-picture @(r/subscribe [:x.user/get-user-profile-picture])]
       [:div.x-user-profile-picture {:style {:backgroundImage (css/url user-profile-picture)}}]))

(defn user-profile-picture-block
  []
  [elements/column ::user-profile-picture-block
                   {:content [:<> [user-profile-picture]
                                  [change-profile-picture-button]]}])

(defn user-name
  []
  [:<> [elements/label ::user-name-label
                       {:content     :name
                        :color       :muted
                        :layout      :fit
                        :font-size   :xs
                        :line-height :block}]
       [elements/button ::user-name-button
                        {:label  "Tech Mono"
                         :preset :default-button
                         :layout :fit}]])

(defn user-email-address
  []
  [:<> [elements/label ::user-email-address-label
                       {:content     :email-address
                        :color       :muted
                        :layout      :fit
                        :font-size   :xs
                        :line-height :block}]
       [elements/button ::user-email-address-button
                        {:label  "demo@monotech.hu"
                         :preset :default-button
                         :layout :fit}]])

(defn user-phone-number
  []
  [:<> [elements/label ::user-phone-number-label
                       {:content     :phone-number
                        :color       :muted
                        :layout      :fit
                        :font-size   :xs
                        :line-height :block}]
       [elements/button ::user-phone-number-button
                        {:label  "+36301234567"
                         :preset :default-button
                         :layout :fit}]])

(defn user-password
  []
  [:<> [elements/label ::user-password-label
                       {:content     :password
                        :color       :muted
                        :layout      :fit
                        :font-size   :xs
                        :line-height :block}]
       [elements/button ::user-password-button
                        {:label  "••••••••"
                         :preset :default-button
                         :layout :fit}]])

(defn user-pin
  []
  [:<> [elements/label ::user-pin-label
                       {:content     :pin
                        :color       :muted
                        :layout      :fit
                        :font-size   :xs
                        :line-height :block}]
       [elements/button ::user-pin-button
                        {:label  "••••"
                         :preset :default-button
                         :layout :fit}]])

(defn body
  [_]
  [:<> [elements/horizontal-separator {:height :s}]
       [user-profile-picture-block]
       [elements/horizontal-separator {:height :s}]
       [elements/horizontal-line      {:color :highlight}]
       [elements/horizontal-separator {:height :l}]
      [:div {:style {:width "100%"}}
       [user-name]
       [elements/horizontal-separator {:height :l}]
       [user-email-address]
       [elements/horizontal-separator {:height :l}]
       [user-phone-number]
       [elements/horizontal-separator {:height :l}]
       [user-password]
       [elements/horizontal-separator {:height :l}]
       [user-pin]
       [elements/horizontal-separator {:height :l}]
       [elements/button ::delete-user-account-button
                        {:label :delete-user-account!
                         :preset :secondary-button}]
       [elements/button ::clear-user-data-button
                        {:label :clear-user-data!
                         :preset :secondary-button}]]])
