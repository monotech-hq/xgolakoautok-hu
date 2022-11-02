
(ns app.price-quotes.frontend.lister.lifecycles
    (:require [app.home.frontend.api]
              [x.app-core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group       :business
                                                            :icon        :request_quote
                                                            :icon-color  "#5da7d9"
                                                            :icon-family :material-icons-outlined
                                                            :label       :price-quotes
                                                            :on-click    [:router/go-to! "/@app-home/price-quotes"]
                                                            :horizontal-weight 2}]
                              [:home.sidebar/add-menu-item! {:group       :business
                                                             :icon        :request_quote
                                                             :icon-color  "#5da7d9"
                                                             :icon-family :material-icons-outlined
                                                             :label       :price-quotes
                                                             :on-click    [:router/go-to! "/@app-home/price-quotes"]
                                                             :vertical-weight 2}]]}})
