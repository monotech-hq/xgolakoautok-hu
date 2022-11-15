
(ns app.price-quotes.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name :business
                                                           ;:icon       :request_page
                                                            :icon       :request_quote
                                                            :icon-color "#5da7d9"
                                                            :label      :price-quotes
                                                            :on-click   [:x.router/go-to! "/@app-home/price-quotes"]
                                                            :horizontal-weight 3}]
                              [:home.sidebar/add-menu-item! {:group-name :business
                                                            ;:icon       :request_page
                                                             :icon       :request_quote
                                                             :icon-color "#5da7d9"
                                                             :label      :price-quotes
                                                             :on-click   [:x.router/go-to! "/@app-home/price-quotes"]
                                                             :vertical-weight 3}]]}})
